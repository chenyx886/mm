package com.mm.data.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "USER_COOR_ENTITY".
*/
public class UserCoorEntityDao extends AbstractDao<UserCoorEntity, Long> {

    public static final String TABLENAME = "USER_COOR_ENTITY";

    /**
     * Properties of entity UserCoorEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property UserId = new Property(1, String.class, "userId", false, "USER_ID");
        public final static Property Phone = new Property(2, String.class, "Phone", false, "PHONE");
        public final static Property DevId = new Property(3, String.class, "devId", false, "DEV_ID");
        public final static Property NetOprt = new Property(4, String.class, "netOprt", false, "NET_OPRT");
        public final static Property Province = new Property(5, String.class, "province", false, "PROVINCE");
        public final static Property CityCode = new Property(6, String.class, "cityCode", false, "CITY_CODE");
        public final static Property City = new Property(7, String.class, "city", false, "CITY");
        public final static Property District = new Property(8, String.class, "district", false, "DISTRICT");
        public final static Property Street = new Property(9, String.class, "street", false, "STREET");
        public final static Property Streetnumber = new Property(10, String.class, "streetnumber", false, "STREETNUMBER");
        public final static Property Lng = new Property(11, Double.class, "lng", false, "LNG");
        public final static Property Lat = new Property(12, Double.class, "lat", false, "LAT");
        public final static Property Address = new Property(13, String.class, "address", false, "ADDRESS");
        public final static Property Status = new Property(14, String.class, "status", false, "STATUS");
        public final static Property LctTime = new Property(15, String.class, "lctTime", false, "LCT_TIME");
        public final static Property Upload = new Property(16, Boolean.class, "upload", false, "UPLOAD");
    }


    public UserCoorEntityDao(DaoConfig config) {
        super(config);
    }
    
    public UserCoorEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"USER_COOR_ENTITY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"USER_ID\" TEXT," + // 1: userId
                "\"PHONE\" TEXT," + // 2: Phone
                "\"DEV_ID\" TEXT," + // 3: devId
                "\"NET_OPRT\" TEXT," + // 4: netOprt
                "\"PROVINCE\" TEXT," + // 5: province
                "\"CITY_CODE\" TEXT," + // 6: cityCode
                "\"CITY\" TEXT," + // 7: city
                "\"DISTRICT\" TEXT," + // 8: district
                "\"STREET\" TEXT," + // 9: street
                "\"STREETNUMBER\" TEXT," + // 10: streetnumber
                "\"LNG\" REAL," + // 11: lng
                "\"LAT\" REAL," + // 12: lat
                "\"ADDRESS\" TEXT," + // 13: address
                "\"STATUS\" TEXT," + // 14: status
                "\"LCT_TIME\" TEXT," + // 15: lctTime
                "\"UPLOAD\" INTEGER);"); // 16: upload
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"USER_COOR_ENTITY\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, UserCoorEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(2, userId);
        }
 
        String Phone = entity.getPhone();
        if (Phone != null) {
            stmt.bindString(3, Phone);
        }
 
        String devId = entity.getDevId();
        if (devId != null) {
            stmt.bindString(4, devId);
        }
 
        String netOprt = entity.getNetOprt();
        if (netOprt != null) {
            stmt.bindString(5, netOprt);
        }
 
        String province = entity.getProvince();
        if (province != null) {
            stmt.bindString(6, province);
        }
 
        String cityCode = entity.getCityCode();
        if (cityCode != null) {
            stmt.bindString(7, cityCode);
        }
 
        String city = entity.getCity();
        if (city != null) {
            stmt.bindString(8, city);
        }
 
        String district = entity.getDistrict();
        if (district != null) {
            stmt.bindString(9, district);
        }
 
        String street = entity.getStreet();
        if (street != null) {
            stmt.bindString(10, street);
        }
 
        String streetnumber = entity.getStreetnumber();
        if (streetnumber != null) {
            stmt.bindString(11, streetnumber);
        }
 
        Double lng = entity.getLng();
        if (lng != null) {
            stmt.bindDouble(12, lng);
        }
 
        Double lat = entity.getLat();
        if (lat != null) {
            stmt.bindDouble(13, lat);
        }
 
        String address = entity.getAddress();
        if (address != null) {
            stmt.bindString(14, address);
        }
 
        String status = entity.getStatus();
        if (status != null) {
            stmt.bindString(15, status);
        }
 
        String lctTime = entity.getLctTime();
        if (lctTime != null) {
            stmt.bindString(16, lctTime);
        }
 
        Boolean upload = entity.getUpload();
        if (upload != null) {
            stmt.bindLong(17, upload ? 1L: 0L);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, UserCoorEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(2, userId);
        }
 
        String Phone = entity.getPhone();
        if (Phone != null) {
            stmt.bindString(3, Phone);
        }
 
        String devId = entity.getDevId();
        if (devId != null) {
            stmt.bindString(4, devId);
        }
 
        String netOprt = entity.getNetOprt();
        if (netOprt != null) {
            stmt.bindString(5, netOprt);
        }
 
        String province = entity.getProvince();
        if (province != null) {
            stmt.bindString(6, province);
        }
 
        String cityCode = entity.getCityCode();
        if (cityCode != null) {
            stmt.bindString(7, cityCode);
        }
 
        String city = entity.getCity();
        if (city != null) {
            stmt.bindString(8, city);
        }
 
        String district = entity.getDistrict();
        if (district != null) {
            stmt.bindString(9, district);
        }
 
        String street = entity.getStreet();
        if (street != null) {
            stmt.bindString(10, street);
        }
 
        String streetnumber = entity.getStreetnumber();
        if (streetnumber != null) {
            stmt.bindString(11, streetnumber);
        }
 
        Double lng = entity.getLng();
        if (lng != null) {
            stmt.bindDouble(12, lng);
        }
 
        Double lat = entity.getLat();
        if (lat != null) {
            stmt.bindDouble(13, lat);
        }
 
        String address = entity.getAddress();
        if (address != null) {
            stmt.bindString(14, address);
        }
 
        String status = entity.getStatus();
        if (status != null) {
            stmt.bindString(15, status);
        }
 
        String lctTime = entity.getLctTime();
        if (lctTime != null) {
            stmt.bindString(16, lctTime);
        }
 
        Boolean upload = entity.getUpload();
        if (upload != null) {
            stmt.bindLong(17, upload ? 1L: 0L);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public UserCoorEntity readEntity(Cursor cursor, int offset) {
        UserCoorEntity entity = new UserCoorEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // userId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // Phone
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // devId
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // netOprt
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // province
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // cityCode
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // city
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // district
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // street
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // streetnumber
            cursor.isNull(offset + 11) ? null : cursor.getDouble(offset + 11), // lng
            cursor.isNull(offset + 12) ? null : cursor.getDouble(offset + 12), // lat
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // address
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // status
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // lctTime
            cursor.isNull(offset + 16) ? null : cursor.getShort(offset + 16) != 0 // upload
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, UserCoorEntity entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUserId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setPhone(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setDevId(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setNetOprt(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setProvince(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setCityCode(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setCity(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setDistrict(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setStreet(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setStreetnumber(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setLng(cursor.isNull(offset + 11) ? null : cursor.getDouble(offset + 11));
        entity.setLat(cursor.isNull(offset + 12) ? null : cursor.getDouble(offset + 12));
        entity.setAddress(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setStatus(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setLctTime(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setUpload(cursor.isNull(offset + 16) ? null : cursor.getShort(offset + 16) != 0);
     }
    
    @Override
    protected final Long updateKeyAfterInsert(UserCoorEntity entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(UserCoorEntity entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(UserCoorEntity entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
