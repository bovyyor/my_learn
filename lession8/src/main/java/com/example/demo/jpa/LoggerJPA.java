package com.example.demo.jpa;

import com.example.demo.entity.LoggerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by bang on 2018/5/11.
 */
public interface LoggerJPA
        extends JpaRepository<LoggerEntity,Long>{
}
