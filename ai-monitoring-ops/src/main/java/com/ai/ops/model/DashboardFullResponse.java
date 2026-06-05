package com.ai.ops.model;

import com.ai.ops.entity.AIDecisionEntity;
import com.ai.ops.entity.AlertEntity;
import com.ai.ops.entity.MetricSnapshotEntity;
import com.ai.ops.entity.ScalingHistoryEntity;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DashboardFullResponse {

    private DashboardSummary summary;

    private List<AlertEntity> alerts;

    private List<ScalingHistoryEntity> scalingHistory;

    private List<AIDecisionEntity> aiDecisions;

    private List<MetricSnapshotEntity> metricsHistory;
}