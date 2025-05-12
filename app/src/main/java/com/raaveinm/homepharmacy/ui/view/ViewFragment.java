package com.raaveinm.homepharmacy.ui.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.raaveinm.homepharmacy.data.Item;
import com.raaveinm.homepharmacy.data.ItemDao;
import com.raaveinm.homepharmacy.data.ItemDatabase;
import com.raaveinm.homepharmacy.databinding.FragmentDashboardBinding;

import java.util.List;

public class ViewFragment extends Fragment {
private FragmentDashboardBinding binding;

    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ItemDatabase db = Room.databaseBuilder(requireContext(), ItemDatabase.class, "database-name").build();
        ItemDao userDao = db.itemDao();
        List<Item> users = userDao.getAllItems();
        final TextView textView = binding.textDashboard;
        for (Item user : users) { System.out.println(user.itemName); }
        return root;
    }

    @Override public void onDestroyView() { super.onDestroyView(); binding = null; }
}