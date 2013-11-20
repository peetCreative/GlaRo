package de.app.infoapp;

import java.io.Serializable;

public class Groupe implements Serializable {

	private static final long serialVersionUID = -6377253317394023789L;

	
	private String mURL;
	private String mName;
	private boolean checked;
	private int id;

	
	public Groupe(String mName, String mURL, boolean checked , int id) {
		super();
		this.mURL = mURL;
		this.mName = mName;
		this.checked = checked;
		this.setId(id);
	}
	
	






	public boolean isChecked() {
		return checked;
	}








	public void setChecked(boolean checked) {
		this.checked = checked;
	}








	@Override
	public boolean equals(Object o) {
		Groupe otherGroupe = (Groupe) o;
		if(this.getmName().equals(otherGroupe.getmName())&&
				this.getmURL().equals(otherGroupe.getmURL())){
			return true;
		}else{
			return false;
		}
		
		
		
	}








	public String getmURL() {
		return mURL;
	}
	public void setmURL(String mURL) {
		this.mURL = mURL;
	}
	public String getmName() {
		return mName;
	}
	public void setmName(String mName) {
		this.mName = mName;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}








	public int getId() {
		return id;
	}








	public void setId(int id) {
		this.id = id;
	}
	
	
}
