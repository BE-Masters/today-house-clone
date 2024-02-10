package com.example.be_study.service.user.service;

import com.example.be_study.service.user.domain.UserMetric;
import com.example.be_study.service.user.dto.UserMetricPagingResponse;
import com.example.be_study.service.user.repository.UserMetricRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserMetricService {
    public UserMetricRepository userMetricRepository;

    public UserMetricService(UserMetricRepository userMetricRepository) {
        this.userMetricRepository = userMetricRepository;
    }

    @Transactional(readOnly = false)
    public void createVeryManyUser() {

        userMetricRepository.saveAllUserBulkMode();
    }

    @Transactional(readOnly = true)
    public Page<UserMetricPagingResponse> paging(Pageable pageable) {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 50; // 한 페이지에 보여줄 데이터 수

        Page<UserMetric> userMetricPage = userMetricRepository.findAll(PageRequest.of(page, pageLimit,
                Sort.by(Sort.Direction.ASC, "userId")));

        Page<UserMetricPagingResponse> userMetricPagingResponsesDto = userMetricPage.map(
                userMetric -> new UserMetricPagingResponse(
                        userMetric.getUserId(),
                        userMetric.getAge(),
                        userMetric.getDeviceType()));

        return userMetricPagingResponsesDto;
    }

    public Map<String, Long> countUsersByAgeGroup() {
        List<Object[]> results = userMetricRepository.countUsersByAgeGroup();

        return results.stream()
                .sorted(Comparator.comparing(result -> getAgeGroupOrder((String) result[0])))
                .collect(Collectors.toMap(
                        result -> (String) result[0], // 연령대
                        result -> (Long) result[1], // 사용자 수
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new)); // 제발 ! 좀! 순서대로! 들어가라고오오오
    }

    private int getAgeGroupOrder(String ageGroup) {
        switch (ageGroup) {
            case "10대":
                return 1;
            case "20대":
                return 2;
            case "30대":
                return 3;
            case "40대":
                return 4;
            case "50대":
                return 5;
            case "60대":
                return 6;
            case "70대":
                return 7;
            case "80대":
                return 8;
            case "90대 이상":
                return 9;
            default:
                return 10;
        }

    }
}
