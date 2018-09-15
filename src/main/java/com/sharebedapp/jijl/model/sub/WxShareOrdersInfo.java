package com.sharebedapp.jijl.model.sub;



import java.util.Date;
import java.util.List;

public class WxShareOrdersInfo {
        private String cTime;
        private List<WxShareOrdersInfo> ListUserShareOrders;
        private Date leaseStartTime;
        private Date leaseEndTime;
        private Integer consumeTime;
        private Double price;

        public Date getLeaseStartTime() {
                return leaseStartTime;
        }

        public void setLeaseStartTime(Date leaseStartTime) {
                this.leaseStartTime = leaseStartTime;
        }

        public Date getLeaseEndTime() {
                return leaseEndTime;
        }

        public void setLeaseEndTime(Date leaseEndTime) {
                this.leaseEndTime = leaseEndTime;
        }

        public String getcTime() {
                return cTime;
        }

        public void setcTime(String cTime) {
                this.cTime = cTime;
        }

        public List<WxShareOrdersInfo> getListUserShareOrders() {
                return ListUserShareOrders;
        }

        public void setListUserShareOrders(List<WxShareOrdersInfo> listUserShareOrders) {
                ListUserShareOrders = listUserShareOrders;
        }

        public Integer getConsumeTime() {
                return consumeTime;
        }

        public void setConsumeTime(Integer consumeTime) {
                this.consumeTime = consumeTime;
        }

        public Double getPrice() {
                return price;
        }

        public void setPrice(Double price) {
                this.price = price;
        }
}
