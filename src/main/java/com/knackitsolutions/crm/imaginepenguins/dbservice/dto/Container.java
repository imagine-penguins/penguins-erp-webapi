package com.knackitsolutions.crm.imaginepenguins.dbservice.dto;

public class Container<T> {
    private String name;
    private Integer location;

    private T data ;

    public Container(){}

    public Container(String name, Integer location, T data) {
        this.name = name;
        this.location = location;
        this.data = data;
    }

    public static class FieldData{
        private Integer integer;

        public FieldData(){}

        public Integer getInteger() {
            return integer;
        }

        public void setInteger(Integer integer) {
            this.integer = integer;
        }
    }

    public static class GraphData{
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

//    public DashboardViewType getDashboardViewType() {
//        return dashboardViewType;
//    }
//
//    public void setDashboardViewType(DashboardViewType dashboardViewType) {
//        this.dashboardViewType = dashboardViewType;
//    }

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
