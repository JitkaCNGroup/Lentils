package dk.cngroup.lentils.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "cypher")
public class Cypher
{
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "stage")
    private int stage;

   @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "codeword")
    private String codeword;

    @Column(name = "hint")
    private String hint;

    public Cypher()
    {
    }

    public Cypher(int stage)
    {
        this.stage = stage;
    }

    public Cypher(String name, int stage, double latitude, double longitude, String codeword, String hint)
    {
        this.name = name;
        this.stage = stage;
        this.latitude = latitude;
        this.longitude = longitude;
        this.codeword = codeword;
        this.hint = hint;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    public int getStage()
    {
        return stage;
    }

    public void setStage(int stage)
    {
        this.stage = stage;
    }

    public String getCodeword()
    {
        return codeword;
    }

    public void setCodeword(String codeword)
    {
        this.codeword = codeword;
    }

    public String getHint()
    {
        return hint;
    }

    public void setHint(String hint)
    {
        this.hint = hint;
    }

    @Override
    public String toString()
    {
        return "Cypher{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", stage=" + stage +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", codeword='" + codeword + '\'' +
                ", hint='" + hint + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cypher cypher = (Cypher) o;
        return id == cypher.id &&
                stage == cypher.stage ;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, stage);
    }
}
