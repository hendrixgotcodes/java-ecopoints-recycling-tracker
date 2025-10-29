import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class Household implements Serializable {

    private String id;
    private String name;
    private String address;
    private LocalDate joinDate;
    private List<RecyclingEvent> events;
    private double totalPoints;

    public Household(String id, String name, String address){
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public LocalDate getJoinDate() { return joinDate; }

    public List<RecyclingEvent> getEvents() { return events; }
    public double getTotalPoints() { return totalPoints; }

    public void addEvent(RecyclingEvent event){
        this.events.add(event);
        this.totalPoints += event.getEcoPoints();
    }

    public double getTotalWeight(){
        double totalWeight = 0.0;
        for(RecyclingEvent event : events){
            totalWeight += event.getWeight();
        }
        return totalWeight;
    }

}
