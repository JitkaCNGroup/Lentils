package dk.cngroup.lentils.entity;

import org.hibernate.validator.constraints.Length;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "team")
public class Team implements Serializable {

    private static final long serialVersionUID = 2479871281907121713L;

    @Id
    @GeneratedValue
    @Column(name = "team_id")
    private Long teamId;

    @Column(name = "name", length = 50, unique = true)
    @NotEmpty(message = "Jméno nesmí být prázdné.")
    @Length(max = 50, message = "Jméno nesmí být delší než 50 znaků.")
    private String name;

    @Column(name = "num_of_members", length = 1)
    @NotNull(message = "Počet členů nesmí být prázdný.")
    @Min(value = 1, message = "Počet členů musí být vetší než 0.")
    private Integer numOfMembers;

    @Column(name = "pin", nullable = false, unique = true)
    private String pin;

    @OneToOne(cascade = CascadeType.ALL)
    private User user;

    public Team() {
    }

    public Team(final String name, final int numOfMembers, final String pin) {
        this.name = name;
        this.numOfMembers = numOfMembers;
        this.pin = pin;
    }

    public Team(
            @NotEmpty
            final String name,
            @NotNull(message = "Počet členů nesmí být prázdný.")
            @Min(value = 1, message = "Počet členů musí být vetší než 0.")
            final Integer numOfMembers
    ) {
        this.name = name;
        this.numOfMembers = numOfMembers;
    }

    public Team(final Long teamId, final String name, final int numOfMembers, final String pin) {
        this.teamId = teamId;
        this.name = name;
        this.numOfMembers = numOfMembers;
        this.pin = pin;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(final Long teamId) {
        this.teamId = teamId;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name.trim();
    }

    public Integer getNumOfMembers() {
        return numOfMembers;
    }

    public void setNumOfMembers(final Integer numOfMembers) {
        this.numOfMembers = numOfMembers;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(final String pin) {
        this.pin = pin;
    }

    @Override
    public String toString() {
        return "Team{" +
                "teamId=" + teamId +
                ", name='" + name + '\'' +
                ", numOfMembers=" + numOfMembers +
                ", pin='" + pin + '\'' +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Team team = (Team) o;
        return Objects.equals(teamId, team.teamId) &&
                Objects.equals(name, team.name) &&
                Objects.equals(numOfMembers, team.numOfMembers) &&
                Objects.equals(pin, team.pin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamId, name, numOfMembers, pin);
    }
}
