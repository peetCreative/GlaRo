package de.app.infoapp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import de.app.infoapp.R;



import android.content.res.Resources;

public class Termin implements Serializable, Comparable<Termin> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6619971026764071483L;
	private Calendar Datum;
	private String Anlass;
	private String GesamtVerantwortlicher;
	private String MusikVeranwortlicher;
	private String EssenVerantworlicher;
	private String Zusatzinfo;
	private boolean Remember;
	private int ID;
	private String NotificationWhen;
	
	
	
	public String getNotificationWhen() {
		return NotificationWhen;
	}
	public void setNotificationWhen(String notification) {
		
			this.NotificationWhen = notification;
		
		
	}
	public Termin(Calendar datum,
			String anlass,
			String gesamtVerantwortlicher,
			String musikVeranwortlicher,
			String essenVerantworlicher,
			String zusatzinfo,
			boolean remember,
			String notification,
			int ID) {
		super();
		this.Datum = datum;
		this.Anlass = anlass;
		this.GesamtVerantwortlicher = gesamtVerantwortlicher;
		this.MusikVeranwortlicher = musikVeranwortlicher;
		this.EssenVerantworlicher = essenVerantworlicher;
		this.Zusatzinfo = zusatzinfo;
		this.Remember = remember;
		this.ID = ID;
		setNotificationWhen(notification);
		
	}
	@Override
	public String toString() {
		return  Anlass ;
	}
	
	
	public String getZusatzinfo() {
		return Zusatzinfo;
	}
	public void setZusatzinfo(String zusatzinfo) {
		Zusatzinfo = zusatzinfo;
	}
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public boolean isRemember() {
		return Remember;
	}
	public void setRemember(boolean remember) {
		this.Remember = remember;
	}
	public Calendar getDatum() {
		return Datum;
	}
	public void setDatum(Calendar datum) {
		Datum = datum;

	}
	public String getAnlass() {
		return Anlass;
	}
	public void setAnlass(String anlass) {
		Anlass = anlass;
	}
	public String getGesamtVerantwortlicher() {
		return GesamtVerantwortlicher;
	}
	public void setGesamtVerantwortlicher(String gesamtVerantwortlicher) {
		GesamtVerantwortlicher = gesamtVerantwortlicher;
	}
	public String getMusikVeranwortlicher() {
		return MusikVeranwortlicher;
	}
	public void setMusikVeranwortlicher(String musikVeranwortlicher) {
		MusikVeranwortlicher = musikVeranwortlicher;
	}
	public String getEssenVerantworlicher() {
		return EssenVerantworlicher;
	}
	public void setEssenVerantworlicher(String essenVerantworlicher) {
		EssenVerantworlicher = essenVerantworlicher;
	}
	@Override
	public boolean equals(Object o) { // benutzt in ArrayList vergleich
		
		try{
			Termin mTermin= (Termin) o;
			if(mTermin.getID()==(this.ID))
				return true;
			else

		return false;

		}catch(ClassCastException e){
			return false;
		}
	}
	
	public boolean equals(Termin mTermin, int i) {
		
				
// mit && implementieren
		
			if(!(mTermin.getID()==(this.ID))){
				//System.out.println("ID"+ this.getAnlass());
				return false;
			}
			if(!mTermin.getDatum().equals(this.Datum)){
				
				return false;
				
			}
			if(!mTermin.isRemember() == this.Remember){
				
				return false;
			}
			if(!(mTermin.getNotificationWhen().equals(this.NotificationWhen))){
				
				return false;
			}
		if(!(i==1)){
			if(!mTermin.getZusatzinfo().equals(this.Zusatzinfo)){
				return false;
			}
			if(!mTermin.getAnlass().equals(this.Anlass)){
				return false;
			}
			if(!mTermin.getGesamtVerantwortlicher().equals(this.GesamtVerantwortlicher)){
				return false;
			}
			if(!mTermin.getMusikVeranwortlicher().equals(this.MusikVeranwortlicher)){
				return false;
			}
			if(!mTermin.getEssenVerantworlicher().equals(this.EssenVerantworlicher)){
				return false;
			}
			
		}
		
		return true;
	}
	
	public String getDateForamted(){
		String formatedDate = this.getDatum().get(Calendar.DATE)+"."+
				(this.getDatum().get(Calendar.MONTH)+1)+"."+
				this.getDatum().get(Calendar.YEAR);
		
		
		
		return formatedDate;
	}
	
	public int getColorbgFormated(Resources res,int position){
		int color;
		switch(getTimespan()){
		case 2: 
			if(position%2==1){
				color = res.getColor(R.color.termine_zeilenfarbe1);
			}
			else{
				color = res.getColor(R.color.termine_zeilenfarbe2);

			}
			break;
		case 1:
			color = res.getColor(R.color.termine_morgen_bg);
			break;
		case 0:
			color = res.getColor(R.color.termine_heute_bg);
			break;
		default:
			color = res.getColor(R.color.termine_schrift_schwarz);
		}
		
		return color;
		
	}
	
	public int getColorWtagSchriftFormated(Resources res){
		int color;
		switch(getTimespan()){
		case 2: 
			color = res.getColor(R.color.termine_schrift_schwarz);
			break;
		case 1:
			color = res.getColor(R.color.termine_morgen_schrift);
			break;
		case 0:
			color = res.getColor(R.color.termine_heute_schrift);
			break;
		default:
			color = res.getColor(R.color.termine_schrift_schwarz);
		}
		
		return color;
	}
	
	public String getWtagFormated(Resources res){
		
		String FormatedWtag;
		
		switch(getTimespan()){
		case 2: 
			FormatedWtag= (this.getDatum().getDisplayName(Calendar.DAY_OF_WEEK,
					Calendar.LONG,
					Locale.GERMANY));
			break;
		case 1:
			FormatedWtag= res.getString(R.string.section_termine_listview_morgen);
			break;
		case 0:
			FormatedWtag = res.getString(R.string.section_termine_listview_heute);
			break;
		default:
			FormatedWtag = "sonstigerFall";
		}
		
		return FormatedWtag;
				
				
	}
	
	public String getTimeFormated(Resources res){
		String TimeFormated;
		if (this.getDatum().get(Calendar.MINUTE)>10)
			TimeFormated=	this.getDatum().get(Calendar.HOUR_OF_DAY)+
					":"+
					this.getDatum().get(Calendar.MINUTE)+
					res.getString(R.string.section_termine_listview_uhr);
		else
			TimeFormated=	this.getDatum().get(Calendar.HOUR_OF_DAY)+
					":0"+
					this.getDatum().get(Calendar.MINUTE)+
					res.getString(R.string.section_termine_listview_uhr);
		return TimeFormated;

	}
	
	public String getPersoinfoFormated(Resources res, int i){
		String persinfo ="", umbruch="";
		if (i == 0){
			umbruch="\n";
		}
		if (!(getGesamtVerantwortlicher().equals("-")))
			persinfo ="Tagesverantworlicher: "+ getGesamtVerantwortlicher();
		if (!(getMusikVeranwortlicher().equals("-"))){
			if(persinfo.equals(""))
				persinfo = "Musik: "+ getMusikVeranwortlicher();
			else
				persinfo =persinfo+ umbruch +" Musik: "+ getMusikVeranwortlicher();
		}
		if (!(getEssenVerantworlicher().equals("-"))){
			if(persinfo.equals(""))
				persinfo = "Essen: "+ getEssenVerantworlicher();
			else
				persinfo =persinfo+ umbruch+" Essen: "+ getEssenVerantworlicher();
				
		}
		
		return persinfo;
		

	}
	
	public int getTimespan(){
		Calendar morgen = Calendar.getInstance();
		morgen.roll(Calendar.DAY_OF_MONTH, 1);
		Calendar heute = Calendar.getInstance();
		
		if(this.getDatum().get(Calendar.DAY_OF_MONTH)==morgen.get(Calendar.DAY_OF_MONTH)&&
				this.getDatum().get(Calendar.MONTH)==morgen.get(Calendar.MONTH) &&
				this.getDatum().get(Calendar.YEAR)== morgen.get(Calendar.YEAR)){
			return 1;
		}
		if(this.getDatum().get(Calendar.DAY_OF_MONTH)==heute.get(Calendar.DAY_OF_MONTH)&&
				this.getDatum().get(Calendar.MONTH)==heute.get(Calendar.MONTH) &&
				this.getDatum().get(Calendar.YEAR)== heute.get(Calendar.YEAR)){
			
			return 0;
		}
		return 2;
			
			

		

		
	}
	
	public static int contains(ArrayList<Termin> List, Termin terminOld){
		for(int i =0; i<List.size(); i++){
			if(List.get(i).equals(terminOld)){
				return i;
			}
		}
		
		return -1;
	}
	public static int containsTime(ArrayList<Termin> List, Termin terminOld){
		for(int i =0; i<List.size(); i++){
			if(List.get(i).equals(terminOld,1)){
				return i;
			}
		}
		
		return -1;
	}

	@Override
	public int compareTo(Termin another) {
		
		Termin ter = (Termin) another;
		if(this.getDatum().after(ter.getDatum())){
			return 1;
		}
		if(this.getDatum().before(ter.getDatum())){
			return -1;
		}
		if(this.getDatum().equals(ter.getDatum())){
			return 0;
		}
		
		
		return 0;
	}

	

}
