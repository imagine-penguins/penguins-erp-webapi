package com.knackitsolutions.crm.imaginepenguins.dbservice.dto;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.DashboardViewType;

import java.util.ArrayList;
import java.util.List;

public class WebDashboardDTO {

    List<Container> containers = new ArrayList<>();

    public static class Container<T extends Type> {
        private String name;
        private DashboardViewType dashboardViewType;
        private Integer location;

        private T data ;

        public Container(){}

        public Container(String name, DashboardViewType dashboardViewType, Integer location, T data) {
            this.name = name;
            this.dashboardViewType = dashboardViewType;
            this.location = location;
            this.data = data;
        }

        public static class FieldData implements Type{
            private Integer integer;

            public FieldData(){}

            public Integer getInteger() {
                return integer;
            }

            public void setInteger(Integer integer) {
                this.integer = integer;
            }
        }

        public static class GraphData implements Type{
            private Point point = new Point();
            private Integer value;

            public GraphData() {}

            public Point getPoint() {
                return point;
            }

            public void setPoint(Point point) {
                this.point = point;
            }

            public Integer getValue() {
                return value;
            }

            public void setValue(Integer value) {
                this.value = value;
            }
        }

        public static class Point {
            private Integer x;
            private Integer y;

            public Point() {
            }

            public Point(Integer x, Integer y) {
                this.x = x;
                this.y = y;
            }

            public Integer getX() {
                return x;
            }

            public void setX(Integer x) {
                this.x = x;
            }

            public Integer getY() {
                return y;
            }

            public void setY(Integer y) {
                this.y = y;
            }
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public DashboardViewType getDashboardViewType() {
            return dashboardViewType;
        }

        public void setDashboardViewType(DashboardViewType dashboardViewType) {
            this.dashboardViewType = dashboardViewType;
        }

        public Integer getLocation() {
            return location;
        }

        public void setLocation(Integer location) {
            this.location = location;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }
    }

    public List<Container> getContainers() {
        return containers;
    }

    public void setContainers(List<Container> containers) {
        this.containers.addAll(containers);
    }

    public void setContainers(Container container) {
        containers.add(container);
    }
}
