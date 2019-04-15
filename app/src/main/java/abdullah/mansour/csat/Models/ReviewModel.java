package abdullah.mansour.csat.Models;

public class ReviewModel
{
    String name;
    float rate;

    public ReviewModel() {
    }

    public ReviewModel(String name, float rate) {
        this.name = name;
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }
}
