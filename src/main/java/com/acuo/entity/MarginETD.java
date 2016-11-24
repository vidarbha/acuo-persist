package com.acuo.entity;

import java.math.BigDecimal;
import java.util.Formatter;
import java.util.List;

public class MarginETD {

    private String type;

    private MarginStatus marginStatus;

    public class MarginStatus
    {
        private String status;

        private List<TimeFrame> timeFrames;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public List<TimeFrame> getTimeFrames() {
            return timeFrames;
        }

        public void setTimeFrames(List<TimeFrame> timeFrames) {
            this.timeFrames = timeFrames;
        }
    }

    public class TimeFrame
    {
        private String timeRangeStart;

        private String timeRangeEnd;

        private int noOfActions;

        private BigDecimal amount;

        private BigDecimal CPTYMargin;

        private BigDecimal EXPMargin;

        private List<Action> actions;

        public String getTimeRangeStart() {
            return timeRangeStart;
        }

        public void setTimeRangeStart(String timeRangeStart) {
            this.timeRangeStart = timeRangeStart;
        }

        public String getTimeRangeEnd() {
            return timeRangeEnd;
        }

        public void setTimeRangeEnd(String timeRangeEnd) {
            this.timeRangeEnd = timeRangeEnd;
        }

        public int getNoOfActions() {
            return noOfActions;
        }

        public void setNoOfActions(int noOfActions) {
            this.noOfActions = noOfActions;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public BigDecimal getCPTYMargin() {
            return CPTYMargin;
        }

        public void setCPTYMargin(BigDecimal CPTYMargin) {
            this.CPTYMargin = CPTYMargin;
        }

        public BigDecimal getEXPMargin() {
            return EXPMargin;
        }

        public void setEXPMargin(BigDecimal EXPMargin) {
            this.EXPMargin = EXPMargin;
        }

        public List<Action> getActions() {
            return actions;
        }

        public void setActions(List<Action> actions) {
            this.actions = actions;
        }
    }

    public class Action
    {
        private String time;

        private String legalEntity;

        private String cpty;

        private String venue;

        private String ccy;

        private BigDecimal initialMargin;

        private BigDecimal variableMargin;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getLegalEntity() {
            return legalEntity;
        }

        public void setLegalEntity(String legalEntity) {
            this.legalEntity = legalEntity;
        }

        public String getCpty() {
            return cpty;
        }

        public void setCpty(String cpty) {
            this.cpty = cpty;
        }

        public String getVenue() {
            return venue;
        }

        public void setVenue(String venue) {
            this.venue = venue;
        }

        public String getCcy() {
            return ccy;
        }

        public void setCcy(String ccy) {
            this.ccy = ccy;
        }

        public BigDecimal getInitialMargin() {
            return initialMargin;
        }

        public void setInitialMargin(BigDecimal initialMargin) {
            this.initialMargin = initialMargin;
        }

        public BigDecimal getVariableMargin() {
            return variableMargin;
        }

        public void setVariableMargin(BigDecimal variableMargin) {
            this.variableMargin = variableMargin;
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public MarginStatus getMarginStatus() {
        return marginStatus;
    }

    public void setMarginStatus(MarginStatus marginStatus) {
        this.marginStatus = marginStatus;
    }
}
