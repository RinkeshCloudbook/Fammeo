package com.fammeo.app.common;

import com.fammeo.app.model.CommonModel;

public interface PassDataInterface {
    public void userData(String fullName, String FN, String LN, String email, String imgUrl);
    public void CityData(CommonModel cityName);
}
