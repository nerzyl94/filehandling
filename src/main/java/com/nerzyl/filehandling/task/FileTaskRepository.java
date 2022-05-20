package com.nerzyl.filehandling.task;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FileTaskRepository extends JpaRepository<FileTaskEntity, Long> {
}
