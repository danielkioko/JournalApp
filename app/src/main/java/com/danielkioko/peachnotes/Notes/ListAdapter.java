package com.danielkioko.peachnotes.Notes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.danielkioko.peachnotes.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListAdapter extends ArrayAdapter<Note> {

    public ArrayList<Note> MainList;

    public ArrayList<Note> noteListTemp;

    public List<Note> nList = null;

    // public ListAdapter.SubjectDataFilter noteDataFilter;

    public ListAdapter(Context context, int id, ArrayList<Note> studentArrayList) {
        super(context, id, studentArrayList);

        this.noteListTemp = new ArrayList<Note>();
        this.noteListTemp.addAll(studentArrayList);
        this.MainList = new ArrayList<Note>();
        this.MainList.addAll(studentArrayList);
    }

//    @Override
//    public Filter getFilter() {
//        if (noteDataFilter == null) {
//            noteDataFilter = new ListAdapter.SubjectDataFilter();
//        }
//        return noteDataFilter;
//    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListAdapter.ViewHolder holder = null;

        if (convertView == null) {

            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.search_list_template, null);

            holder = new ListAdapter.ViewHolder();
            holder.theTitle = convertView.findViewById(R.id.tvFileName);
            holder.theDate = convertView.findViewById(R.id.tvFileSaveDate);

            convertView.setTag(holder);

        } else {

            holder = (ListAdapter.ViewHolder) convertView.getTag();
        }

        Note note = noteListTemp.get(position);
        holder.theTitle.setText(note.getTitle());
        holder.theDate.setText(note.getDate());

        return convertView;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        MainList.clear();
        if (charText.length() == 0) {
            noteListTemp.addAll(MainList);
        } else {
            for (Note n : MainList) {
                if (n.getTitle().toLowerCase(Locale.getDefault()).contains(charText)) {
                    MainList.add(n);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class ViewHolder {
        TextView theTitle;
        TextView theDate;
    }

//    private class SubjectDataFilter extends Filter {
//
//        @Override
//        protected FilterResults performFiltering(CharSequence charSequence) {
//
//            charSequence = charSequence.toString().toLowerCase();
//            FilterResults filterResults = new FilterResults();
//
//            if (charSequence != null && charSequence.toString().length() > 0) {
//                ArrayList<Note> arrayList1 = new ArrayList<Note>();
//
//                for (int i = 0, l = MainList.size(); i < l; i++) {
//                    Note subject = MainList.get(i);
//
//                    if (subject.toString().toLowerCase().contains(charSequence))
//
//                        arrayList1.add(subject);
//                }
//                filterResults.count = arrayList1.size();
//                filterResults.values = arrayList1;
//
//            } else {
//                synchronized (this) {
//                    filterResults.values = MainList;
//
//                    filterResults.count = MainList.size();
//                }
//            }
//            return filterResults;
//        }
//
//        @SuppressWarnings("unchecked")
//        @Override
//        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//
//            noteListTemp = (ArrayList<Note>) filterResults.values;
//            notifyDataSetChanged();
//
//            clear();
//
//            for (int i = 0, l = noteListTemp.size(); i < l; i++)
//                add(noteListTemp.get(i));
//
//            notifyDataSetInvalidated();
//        }
//
//    }
}