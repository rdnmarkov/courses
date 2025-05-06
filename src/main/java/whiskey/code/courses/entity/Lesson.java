package whiskey.code.courses.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Entity
@Table(name = "lessons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lesson {

    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", orderNumber=" + orderNumber +
                ", messageIds=" + messageIds +
                '}';
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lesson_id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "order_number", nullable = false)
    private Integer orderNumber;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "message_id", nullable = false)
    private List<Integer> messageIds;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;
}
