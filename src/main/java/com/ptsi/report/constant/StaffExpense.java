package com.ptsi.report.constant;

public enum StaffExpense {


    DPR_NO("DPR No."),
    DATE("Date"),
    PROJECT_NO("Project No."),
    PROJECT_NAME("Project"),
    FLOOR_LEVEL("Floor Level"),
    CITY("City"),
    PROJECT_COORDINATOR("Project Coordinator"),
    FIELD_STAFF_NAME("Field Staff Name"),
    ACTUAL_EXPENSE("Actual Expense"),
    DPR_STATUS("DPR Status"),
    TEA_FOOD_EXPENSE_ENGG("01. Tea/Food Expense - Engg."),
    TEA_FOOD_EXPENSE_WORKER("02. Tea/Food Expense - Worke"),
    HOTEL_CHARGES_ENGG("03. Hotel Charges - Engg."),
    HOTEL_CHARGES_WORKER("04. Hotel Charges - Worker"),
    RAILWAY_BUS_FARE_ENGG("05. Rly / Bus Fare - Engg."),
    RAILWAY_BUS_FARE_WORKER("06. Rly / Bus Fare - Worker"),
    AUTO_FARE_ENGG("07. Auto Fare - Engg."),
    AUTO_FARE_WORKER("08. Auto Fare - Worker"),
    LOCAL_PURCHASE_ENGG("09. Local Purchase - Engg."),
    LOCAL_PURCHASE_WORKER("10. Local Purchase - Worker"),
    RENT_ENGG("11. Rent - Engg."),
    RENT_WORKER("12. Rent - Worker"),
    MATERIAL_UNLOAD_UPLIFT("13. Material Unload / Uplift"),
    NET_AND_PRINTING("14. Net & Printing"),
    LUGGAGE_CHARGES("15. Luggage Charges"),
    POSTAGE_AND_COURIER("16. Postage & Courier"),
    CNG_PETROL_TOLL_TAX("17. CNG / Petrol / Toll Tax"),
    ADVANCE_TO_ENGINEER("18. Advance To Engineer"),
    REPAIRS_AND_MAINTENANCE("19. Repairs & Maintenance"),
    WORKER_FOOD("20. Worker Food"),
    WORKER_FOOD_CONTRACTOR("21. Worker Food (Contractor)"),
    ELECTRICITY_CHARGES("22. Electricity Charges"),
    OFFICE_EXPENSES("23. Office Expenses"),
    MOBILE_CHARGES_DONGLE("24. Mobile Charges [ Dongle "),
    MEDICAL_EXPENSE_ENGG("25. Medical Expense - Engg."),
    MEDICAL_EXPENSE_WORKER("26. Medical Expense - Worker"),
    WORKER_SALARY("27. Worker  Salary."),
    STAFF_WELFARE_COOK_ETC("28. Staff Welfare (Cook etc."),
    OTHER_CHARGES("29. Other Charges"),
    MTH_CONVEY_ALLOWNC("30.Mth Convey. Allownc."),
    MTH_TEA_SNCK_ALLOWNC("31.Mth.Tea-Snck. Allownc."),
    MTH_TELE_MOB_ALLOWNC("32.Mth.Tele/Mob. Allownc."),
    DAY_TOTAL("Day Total");



    private final String name;

    StaffExpense(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

}
