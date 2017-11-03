package com.noordwind.apps.collectively.presentation.addremark.tags;


import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.noordwind.apps.collectively.R;

import org.zakariya.stickyheaders.SectioningAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * SimpleDemoAdapter, just shows demo data
 */
public class StickyTagsAdapter extends SectioningAdapter {

    private class Section {
        int index;
        int copyCount;
        String header;
        String footer;
        ArrayList<String> items = new ArrayList<>();
    }

    ArrayList<Section> sections = new ArrayList<>();

    public StickyTagsAdapter(Map<String, List<String>> groupedTags) {
        int index = 0;

        List<String> sortedKeys = new LinkedList<>(groupedTags.keySet());
        Collections.sort(sortedKeys, new Comparator<String>() {
            @Override
            public int compare(String s, String s2) {
                return s.compareTo(s2);
            }
        });

        for (String key : sortedKeys) {
            Section section = new Section();
            section.index = index;
            section.copyCount = 0;
            section.header = key;
            section.items.addAll(groupedTags.get(key));
            sections.add(section);
        }
    }

    @Override
    public int getNumberOfSections() {
        return sections.size();
    }

    @Override
    public int getNumberOfItemsInSection(int sectionIndex) {
        return sections.get(sectionIndex).items.size();
    }

    @Override
    public boolean doesSectionHaveHeader(int sectionIndex) {
        return !TextUtils.isEmpty(sections.get(sectionIndex).header);
    }

    @Override
    public boolean doesSectionHaveFooter(int sectionIndex) {
        return !TextUtils.isEmpty(sections.get(sectionIndex).footer);
    }

    @Override
    public ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int itemType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.view_tag_list_item_row, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent, int headerType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.view_tag_list_header_row, parent, false);
        return new HeaderViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindItemViewHolder(SectioningAdapter.ItemViewHolder viewHolder, int sectionIndex, int itemIndex, int itemType) {
        Section section = sections.get(sectionIndex);
        ItemViewHolder holder = (ItemViewHolder) viewHolder;
        holder.nameLabel.setText(section.items.get(itemIndex));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindHeaderViewHolder(SectioningAdapter.HeaderViewHolder viewHolder, int sectionIndex, int headerType) {
        Section section = sections.get(sectionIndex);
        HeaderViewHolder holder = (HeaderViewHolder) viewHolder;
        holder.nameLabel.setText(section.header);
    }

    public class ItemViewHolder extends SectioningAdapter.ItemViewHolder {

        TextView nameLabel;

        public ItemViewHolder(View itemView) {
            super(itemView);
            nameLabel = (TextView) itemView.findViewById(R.id.name);
        }

    }

    public class HeaderViewHolder extends SectioningAdapter.HeaderViewHolder {
        TextView nameLabel;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            nameLabel = (TextView) itemView.findViewById(R.id.name);
        }
    }
}

