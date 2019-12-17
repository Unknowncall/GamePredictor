
public class Team {
int id;
String name;
public Team(int id, String name) {
	super();
	this.id = id;
	this.name = name;
}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
@Override
public boolean equals(Object o){
    if(o instanceof Team){
         Team p = (Team) o;
         return this.name.equals(p.getName());
    } else
         return false;
}
}
