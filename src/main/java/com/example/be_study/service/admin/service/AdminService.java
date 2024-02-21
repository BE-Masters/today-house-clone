package com.example.be_study.service.admin.service;

import com.example.be_study.common.response.DataResponse;
import com.example.be_study.service.admin.dto.*;
import com.example.be_study.service.admin.enums.AdminResponseMessage;
import com.example.be_study.service.admin.repository.AdminUserCustomRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {

    private final AdminUserCustomRepository adminUserCustomRepository;

    private final ModelMapper modelMapper;

    public AdminService(AdminUserCustomRepository adminUserCustomRepository, ModelMapper modelMapper) {
        this.adminUserCustomRepository = adminUserCustomRepository;
        this.modelMapper = modelMapper;
    }

    /**
     *  사용자 목록 조회
     */
    @Transactional(readOnly = true)
    public DataResponse<AdminUserListResponse> adminUserList(Pageable request) {
        Page<AdminUserInfoDto> userList = adminUserCustomRepository.findAll(request);

        PagingDto pagingDto = modelMapper.map(userList, PagingDto.class);

        return new DataResponse<>(AdminResponseMessage.SUCCESS, AdminUserListResponse.of(pagingDto, userList.getContent()));
    }

    /**
     * 사용자 연령대별 카운트
     */
    @Transactional(readOnly = true)
    public DataResponse<UserAgeCount> userAgeCount() {
        return new DataResponse<>(AdminResponseMessage.SUCCESS, adminUserCustomRepository.userAgeCount());
    }
}
