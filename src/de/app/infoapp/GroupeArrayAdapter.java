package de.app.infoapp;

import de.app.infoapp.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;



public class GroupeArrayAdapter extends ArrayAdapter<Groupe> {

	
	private Context context;
	private ListView lv;
	static int anzahl = 0;
	
	
		
		public GroupeArrayAdapter(Context context,  ListView lv){
			super(context, R.layout.dialog_main_setup_grouperow, JGApplication.getmGroupeArrayList());
			
			this.context = context;
			this.lv = lv;
			anzahl++;
			//System.out.println("anzahl: "+ GroupeArrayAdapter.anzahl);
			
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View itemView = convertView;
			
			final GroupeHolder holder = new GroupeHolder();
			
			if (itemView == null){
				
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				itemView = inflater.inflate(R.layout.dialog_main_setup_grouperow, parent, false);
				
				CheckBox checkbox = (CheckBox) itemView.findViewById(R.id.dialog_main_setup_grouperow_groupeCB);
				holder.cb = checkbox;
				Groupe groupe = JGApplication.getmGroupeArrayList().get(position);
				holder.cb.setText(groupe.getmName());
				holder.cb.setChecked(groupe.isChecked());
				MyOnCheckedChangedListener listener = new MyOnCheckedChangedListener(lv);
				holder.cb.setOnCheckedChangeListener(listener); // hier ändern
				itemView.setTag(holder);
				//System.out.println("groupe is Checked"+ groupe.isChecked() + groupe.getmName());
				//System.out.println("CheckBox"+ holder.cb.isChecked() );
				
			}
			
			//System.out.println("immer");
			
			

			return itemView;
		}
		
		private static class GroupeHolder{
			public CheckBox cb;
			
		}


	


}
