package com.alpha.www.ElectronicStore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alpha.www.ElectronicStore.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, String> {

}
