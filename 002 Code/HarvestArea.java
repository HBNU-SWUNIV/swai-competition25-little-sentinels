package com.example.growvision;

public class HarvestArea {
    private String area_name;
    private long total_num;
    private long ripe_num;
    private long unripe_num;
    private String rail_code;
    private String rail_path;

    public HarvestArea(String area_name, long total_num, long ripe_num, long unripe_num, String rail_code, String rail_path) {
        this.area_name = area_name;
        this.total_num = total_num;
        this.ripe_num = ripe_num;
        this.unripe_num = unripe_num;
        this.rail_code = rail_code;
        this.rail_path = rail_path;
    }

    // 기존 스네이크 케이스 게터
    public String getArea_name()    { return area_name; }
    public long   getTotal_num()    { return total_num; }
    public long   getRipe_num()     { return ripe_num; }
    public long   getUnripe_num()   { return unripe_num; }
    public String getRail_code()    { return rail_code; }
    public String getRail_path()    { return rail_path; }

    // ──────────────── 추가된 camel-case 게터 ────────────────
    public String getAreaName()     { return area_name; }
    public long   getTotalNum()     { return total_num; }
    public long   getRipeNum()      { return ripe_num; }
    public long   getUnripeNum()    { return unripe_num; }
    public String getRailCode()     { return rail_code; }
    public String getRailPath()     { return rail_path; }
}
