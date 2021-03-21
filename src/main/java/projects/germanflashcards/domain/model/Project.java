package projects.germanflashcards.domain.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "projects",
        uniqueConstraints = @UniqueConstraint(
                name = "projects_name_user_id_idx",
                columnNames = {"name", "user_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString(exclude = "user")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    private String url;
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
