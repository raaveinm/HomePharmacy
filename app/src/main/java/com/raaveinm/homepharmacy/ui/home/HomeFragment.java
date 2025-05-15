package com.raaveinm.homepharmacy.ui.home;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import com.raaveinm.homepharmacy.R;
import com.raaveinm.homepharmacy.data.Item;
import com.raaveinm.homepharmacy.data.ItemDao;
import com.raaveinm.homepharmacy.data.ItemDatabase;
import com.raaveinm.homepharmacy.databinding.FragmentHomeBinding;
import com.raaveinm.homepharmacy.domain.RecyclerViewAdapter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private RecyclerViewAdapter recyclerViewAdapter;
    private ItemDao itemDao;
    private ExecutorService executorService;

    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        executorService = Executors.newSingleThreadExecutor();

        recyclerViewAdapter = new RecyclerViewAdapter();
        binding.viewFragmentRecycleView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.viewFragmentRecycleView.setAdapter(recyclerViewAdapter);

        recyclerViewAdapter.setOnItemClickListener(this::showEditDialog);

        ItemDatabase db = Room.databaseBuilder(
                requireContext().getApplicationContext(),
                ItemDatabase.class,
                "database-name"
        ).build();

        itemDao = db.itemDao(); // Assign to class member itemDao

        itemDao.getAllItems().observe(getViewLifecycleOwner(), items -> {
            if (items != null) {
                recyclerViewAdapter.dataChanged(items);
                for (Item item : items) {
                    System.out.println("Item Fetched: " + item.itemName);
                }
            }
        });
    }

    private void showEditDialog(Item item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_item, null);
        builder.setView(dialogView);

        EditText editTextItemName = dialogView.findViewById(R.id.editTextItemNameDialog);
        EditText editTextCompanyName = dialogView.findViewById(R.id.editTextCompanyNameDialog);
        EditText editTextItemQuantity = dialogView.findViewById(R.id.editTextItemQuantityDialog);
        EditText editTextItemPrice = dialogView.findViewById(R.id.editTextItemPriceDialog);
        Button buttonSave = dialogView.findViewById(R.id.buttonSaveItemDialog);
        Button buttonCancel = dialogView.findViewById(R.id.buttonCancelItemDialog);

        editTextItemName.setText(item.itemName);
        editTextCompanyName.setText(item.companyName);
        editTextItemQuantity.setText(String.valueOf(item.itemQuantity));
        editTextItemPrice.setText(String.valueOf(item.itemPrice));

        AlertDialog alertDialog = builder.create();

        buttonSave.setOnClickListener(v -> {
            String name = editTextItemName.getText().toString().trim();
            String company = editTextCompanyName.getText().toString().trim();
            String quantityStr = editTextItemQuantity.getText().toString().trim();
            String priceStr = editTextItemPrice.getText().toString().trim();

            if (name.isEmpty() || company.isEmpty() || quantityStr.isEmpty() || priceStr.isEmpty()) {
                Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int quantity = Integer.parseInt(quantityStr);
                int price = Integer.parseInt(priceStr);

                item.itemName = name;
                item.companyName = company;
                item.itemQuantity = quantity;
                item.itemPrice = price;

                executorService.execute(() -> {
                    itemDao.update(item);
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), "Item updated", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        });
                    }
                });
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Invalid number format for quantity or price", Toast.LENGTH_SHORT).show();
            }
        });

        buttonCancel.setOnClickListener(v -> alertDialog.dismiss());
        alertDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}