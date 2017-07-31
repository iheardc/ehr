package main;


import java.util.Date;

public class Forms {
    
    String name, type, size;
    String date;
    Forms(String n, String s){
        this.name = n;
        this.size = s;

    }    

    Forms(String n, String s, String d){
        this.name = n;
        this.size = s;
        this.date = d;
    }
    public String getExtension(){
        int l = this.name.lastIndexOf(".");
        //System.out.println("!!!!!!!!!!!!" + this.name);
        String ext = this.name.substring(l, this.name.length());
        return ext;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}

