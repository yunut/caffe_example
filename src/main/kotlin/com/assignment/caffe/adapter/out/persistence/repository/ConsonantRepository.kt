package com.assignment.caffe.adapter.out.persistence.repository

import com.assignment.caffe.application.domain.model.Consonant
import org.springframework.data.jpa.repository.JpaRepository

interface ConsonantRepository : JpaRepository<Consonant, String>
