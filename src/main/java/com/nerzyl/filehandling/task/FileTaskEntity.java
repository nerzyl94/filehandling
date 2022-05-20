package com.nerzyl.filehandling.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "file_tasks")
public class FileTaskEntity {
    @Id
    @SequenceGenerator(
            name = "file_task_id_sequence",
            sequenceName = "file_task_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "file_task_id_sequence"
    )
    private Long id;
    @CreationTimestamp
    private LocalDateTime createDate;
    private String fileName;
    private Integer supply;
    private Integer buy;
    private Integer result;
}



