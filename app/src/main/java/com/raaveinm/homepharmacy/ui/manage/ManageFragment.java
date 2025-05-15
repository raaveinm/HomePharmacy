package com.raaveinm.homepharmacy.ui.manage;

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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.snackbar.Snackbar;
import com.raaveinm.homepharmacy.R;
import com.raaveinm.homepharmacy.data.Item;
import com.raaveinm.homepharmacy.data.ItemDao;
import com.raaveinm.homepharmacy.data.ItemDatabase;
import com.raaveinm.homepharmacy.databinding.FragmentNotificationsBinding;
import com.raaveinm.homepharmacy.domain.RecyclerViewAdapter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ManageFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private ItemDao itemDao;
    private RecyclerViewAdapter recyclerViewAdapter;
    private ExecutorService executorService;

    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        executorService = Executors.newSingleThreadExecutor();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ItemDatabase db = Room.databaseBuilder(
                requireContext().getApplicationContext(),
                ItemDatabase.class,
                "database-name"
        ).build();
        itemDao = db.itemDao();

        recyclerViewAdapter = new RecyclerViewAdapter();
        binding.manageRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.manageRecyclerView.setAdapter(recyclerViewAdapter);


        itemDao.getAllItems().observe(getViewLifecycleOwner(), items -> {
            if (items != null) { recyclerViewAdapter.dataChanged(items); }
        });

        binding.buttonAddItem.setOnClickListener(v -> showAddItemDialog());

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(
                    @NonNull RecyclerView recyclerView,
                    @NonNull RecyclerView.ViewHolder viewHolder,
                    @NonNull RecyclerView.ViewHolder target
            ){ return false; }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Item itemToDelete = recyclerViewAdapter.getItemAt(position);

                if (itemToDelete != null) {
                    executorService.execute(() -> {
                        itemDao.delete(itemToDelete);
                        requireActivity().runOnUiThread(() -> Snackbar.make(binding.getRoot(),
                                        "Item deleted: " + itemToDelete.itemName,
                                        Snackbar.LENGTH_LONG)
                                .setAction("UNDO", v -> executorService.execute(() ->
                                        itemDao.insert(itemToDelete)))
                                .show());
                    });
                }
            }
        }).attachToRecyclerView(binding.manageRecyclerView);
    }

    private void showAddItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_item, null);
        builder.setView(dialogView);

        EditText editTextItemName = dialogView.findViewById(R.id.editTextItemNameAddDialog);
        EditText editTextCompanyName = dialogView.findViewById(R.id.editTextCompanyNameAddDialog);
        EditText editTextItemQuantity = dialogView.findViewById(R.id.editTextItemQuantityAddDialog);
        EditText editTextItemPrice = dialogView.findViewById(R.id.editTextItemPriceAddDialog);
        Button buttonAddItemDialog = dialogView.findViewById(R.id.buttonAddItemDialog);
        Button buttonCancelAddDialog = dialogView.findViewById(R.id.buttonCancelAddDialog);


        AlertDialog alertDialog = builder.create();

        buttonAddItemDialog.setOnClickListener(v -> {
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

                Item newItem = new Item();
                newItem.itemName = name;
                newItem.companyName = company;
                newItem.itemQuantity = quantity;
                newItem.itemPrice = price;

                executorService.execute(() -> {
                    itemDao.insert(newItem);
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Item added", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    });
                });
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Invalid number format for quantity or price",
                        Toast.LENGTH_SHORT).show();
            }
        });
        buttonCancelAddDialog.setOnClickListener(v -> alertDialog.dismiss());
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