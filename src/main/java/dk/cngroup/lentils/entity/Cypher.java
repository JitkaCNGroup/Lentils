package dk.cngroup.lentils.entity;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.geo.Point;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "cypher")
public class Cypher implements Serializable {

    private static final long serialVersionUID = 1565385429261533400L;

    @Id
    @GeneratedValue
    @Column(name = "cypher_id")
    private Long cypherId;

    @OneToMany(mappedBy = "cypher")
    private List<Hint> hints;

    @Column(name = "name")
    private String name;

    @Column(name = "stage")
    @NotNull(message = "Pořadí nesmí být prázdné.")
    @Min(value = 1, message = "Pořadí musí být větší než 0.")
    private int stage;

    @Column(name = "location")
    @NotNull(message = "Souřadnice nesmí být prázdné.")
    private Point location;

    @Column(name = "place_description", length = 2000)
    @Length(max = 2000, message = "Popis místa nesmí být delší než 2000 znaků.")
    private String placeDescription;

    @Column(name = "codeword")
    private String codeword;

    @Column(name = "trap_codeword")
    private String trapCodeword;

    @Column(name = "bonus_content", length = 4000)
    @Length(max = 4000, message = "Bonusové informace nesmí být delší než 4000 znaků.")
    private String bonusContent;

    @Column(name = "map_address")
    @NotEmpty(message = "Adresa mapy nesmí být prázdná.")
    private String mapAddress;

    @OrderBy("username")
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "cypher_organizers",
            joinColumns = @JoinColumn(name = "cypher_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> organizers;

    public Cypher() {
    }

    public Cypher(final Point location, final int stage, final String mapAddress) {
        this.location = location;
        this.stage = stage;
        this.mapAddress = mapAddress;
    }

    public Cypher(final String name,
                  final int stage,
                  final Point location,
                  final String codeword,
                  final String mapAddress) {
        this.name = name;
        this.stage = stage;
        this.location = location;
        this.codeword = codeword;
        this.mapAddress = mapAddress;
    }

    public Cypher(final Point location,
                  final int stage,
                  final String bonusContent,
                  final String placeDescription,
                  final String mapAddress) {
        this.location = location;
        this.stage = stage;
        this.bonusContent = bonusContent;
        this.placeDescription = placeDescription;
        this.mapAddress = mapAddress;
    }

    public Cypher(
            final List<Hint> hints,
            final String name,
            final int stage,
            final Point location,
            final String codeword
    ) {
        this.hints = hints;
        this.name = name;
        this.stage = stage;
        this.location = location;
        this.codeword = codeword;
    }

    public String getMapAddress() {
        return mapAddress;
    }

    public void setMapAddress(final String mapAddress) {
        this.mapAddress = mapAddress;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(final Point location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(final int stage) {
        this.stage = stage;
    }

    public String getCodeword() {
        return codeword;
    }

    public void setCodeword(final String codeword) {
        this.codeword = codeword;
    }

    public String getTrapCodeword() {
        return trapCodeword;
    }

    public void setTrapCodeword(final String trapCodeword) {
        this.trapCodeword = trapCodeword;
    }

    public Long getCypherId() {
        return cypherId;
    }

    public void setCypherId(final Long cypherId) {
        this.cypherId = cypherId;
    }

    public List<Hint> getHints() {
        return hints;
    }

    public void setHints(final List<Hint> hints) {
        this.hints = hints;
    }

    public void addHint(final Hint hint) {
        hints.add(hint);
    }

    public String getBonusContent() {
        return bonusContent;
    }

    public void setBonusContent(final String bonusContent) {
        this.bonusContent = bonusContent;
    }

    public String getPlaceDescription() {
        return placeDescription;
    }

    public void setPlaceDescription(final String placeDescription) {
        this.placeDescription = placeDescription;
    }

    public List<User> getOrganizers() {
        return organizers;
    }

    public void setOrganizers(final List<User> organizer) {
        this.organizers = organizer;
    }

    @Override
    public String toString() {
        return "Cypher{" +
                "cypherId=" + cypherId +
                ", hints=" + hints +
                ", name='" + name + '\'' +
                ", stage=" + stage +
                ", location=" + location +
                ", placeDescription='" + placeDescription + '\'' +
                ", codeword='" + codeword + '\'' +
                ", trapCodeword='" + trapCodeword + '\'' +
                ", bonusContent='" + bonusContent + '\'' +
                ", mapAddress='" + mapAddress + '\'' +
                ", organizer=" + organizers +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cypher)) {
            return false;
        }
        Cypher cypher = (Cypher) o;
        return stage == cypher.stage &&
                Objects.equals(cypherId, cypher.cypherId) &&
                Objects.equals(hints, cypher.hints) &&
                Objects.equals(name, cypher.name) &&
                Objects.equals(location, cypher.location) &&
                Objects.equals(placeDescription, cypher.placeDescription) &&
                Objects.equals(codeword, cypher.codeword) &&
                Objects.equals(trapCodeword, cypher.trapCodeword) &&
                Objects.equals(bonusContent, cypher.bonusContent) &&
                Objects.equals(mapAddress, cypher.mapAddress) &&
                Objects.equals(organizers, cypher.organizers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cypherId,
                hints,
                name,
                stage,
                location,
                placeDescription,
                codeword,
                trapCodeword,
                bonusContent,
                mapAddress,
                organizers);
    }
}