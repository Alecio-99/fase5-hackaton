package com.fase5.hackton.presentation.resource;

import com.fase5.hackton.application.service.DashboardService;
import com.fase5.hackton.presentation.api.DashboardApi;
import com.fase5.hackton.presentation.api.model.DashboardResponse;
import com.fase5.hackton.presentation.mapper.PresentationMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DashboardResource implements DashboardApi {
	private final DashboardService dashboardService;

	public DashboardResource(DashboardService dashboardService) {
		this.dashboardService = dashboardService;
	}

	@Override
	public ResponseEntity<DashboardResponse> obterDashboard() {
		return ResponseEntity.ok(PresentationMapper.toResponse(dashboardService.obter()));
	}
}
