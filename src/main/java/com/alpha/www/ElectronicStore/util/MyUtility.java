package com.alpha.www.ElectronicStore.util;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import com.alpha.www.ElectronicStore.dto.PageableResponse;

public class MyUtility {

	public static <E, D> PageableResponse<D> getPageableResponse(Page<E> page, Class<D> type){
		
		List<E> entity = page.getContent();
		List<D> dtoList = entity.stream().map(object -> new ModelMapper().map(object, type)).collect(Collectors.toList());
		
		PageableResponse<D> response = new PageableResponse<>();
		response.setContent(dtoList);
		response.setPageNo(page.getNumber());
		response.setPageSize(page.getSize());
		response.setTotalElements(page.getTotalElements());
		response.setTotalPages(page.getTotalPages());
		response.setLastPage(page.isLast());
		
		return response;
	}
}
