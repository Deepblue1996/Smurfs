package com.prohua.smurfs.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "STUDENT_INFO".
*/
public class StudentInfoDao extends AbstractDao<StudentInfo, Long> {

    public static final String TABLENAME = "STUDENT_INFO";

    /**
     * Properties of entity StudentInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property StudentNo = new Property(1, String.class, "studentNo", false, "stud_no");
        public final static Property LengthOfSchooling = new Property(2, String.class, "LengthOfSchooling", false, "length_of_schooling");
        public final static Property NickName = new Property(3, String.class, "nickName", false, "nickname");
        public final static Property Sex = new Property(4, String.class, "sex", false, "sex");
        public final static Property UId = new Property(5, String.class, "uId", false, "uid");
        public final static Property Phone = new Property(6, String.class, "phone", false, "phone");
        public final static Property HeadImgUrl = new Property(7, String.class, "headImgUrl", false, "headimgurl");
        public final static Property Intro = new Property(8, String.class, "intro", false, "intro");
        public final static Property Grade = new Property(9, String.class, "grade", false, "grade");
        public final static Property ClientId = new Property(10, String.class, "clientId", false, "clientid");
        public final static Property LastId = new Property(11, String.class, "lastId", false, "lastid");
        public final static Property Integral = new Property(12, String.class, "integral", false, "integral");
        public final static Property Token = new Property(13, String.class, "token", false, "token");
        public final static Property State = new Property(14, boolean.class, "state", false, "state");
    }


    public StudentInfoDao(DaoConfig config) {
        super(config);
    }
    
    public StudentInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"STUDENT_INFO\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"stud_no\" TEXT," + // 1: studentNo
                "\"length_of_schooling\" TEXT," + // 2: LengthOfSchooling
                "\"nickname\" TEXT," + // 3: nickName
                "\"sex\" TEXT," + // 4: sex
                "\"uid\" TEXT," + // 5: uId
                "\"phone\" TEXT," + // 6: phone
                "\"headimgurl\" TEXT," + // 7: headImgUrl
                "\"intro\" TEXT," + // 8: intro
                "\"grade\" TEXT," + // 9: grade
                "\"clientid\" TEXT," + // 10: clientId
                "\"lastid\" TEXT," + // 11: lastId
                "\"integral\" TEXT," + // 12: integral
                "\"token\" TEXT," + // 13: token
                "\"state\" INTEGER NOT NULL );"); // 14: state
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"STUDENT_INFO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, StudentInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String studentNo = entity.getStudentNo();
        if (studentNo != null) {
            stmt.bindString(2, studentNo);
        }
 
        String LengthOfSchooling = entity.getLengthOfSchooling();
        if (LengthOfSchooling != null) {
            stmt.bindString(3, LengthOfSchooling);
        }
 
        String nickName = entity.getNickName();
        if (nickName != null) {
            stmt.bindString(4, nickName);
        }
 
        String sex = entity.getSex();
        if (sex != null) {
            stmt.bindString(5, sex);
        }
 
        String uId = entity.getUId();
        if (uId != null) {
            stmt.bindString(6, uId);
        }
 
        String phone = entity.getPhone();
        if (phone != null) {
            stmt.bindString(7, phone);
        }
 
        String headImgUrl = entity.getHeadImgUrl();
        if (headImgUrl != null) {
            stmt.bindString(8, headImgUrl);
        }
 
        String intro = entity.getIntro();
        if (intro != null) {
            stmt.bindString(9, intro);
        }
 
        String grade = entity.getGrade();
        if (grade != null) {
            stmt.bindString(10, grade);
        }
 
        String clientId = entity.getClientId();
        if (clientId != null) {
            stmt.bindString(11, clientId);
        }
 
        String lastId = entity.getLastId();
        if (lastId != null) {
            stmt.bindString(12, lastId);
        }
 
        String integral = entity.getIntegral();
        if (integral != null) {
            stmt.bindString(13, integral);
        }
 
        String token = entity.getToken();
        if (token != null) {
            stmt.bindString(14, token);
        }
        stmt.bindLong(15, entity.getState() ? 1L: 0L);
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, StudentInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String studentNo = entity.getStudentNo();
        if (studentNo != null) {
            stmt.bindString(2, studentNo);
        }
 
        String LengthOfSchooling = entity.getLengthOfSchooling();
        if (LengthOfSchooling != null) {
            stmt.bindString(3, LengthOfSchooling);
        }
 
        String nickName = entity.getNickName();
        if (nickName != null) {
            stmt.bindString(4, nickName);
        }
 
        String sex = entity.getSex();
        if (sex != null) {
            stmt.bindString(5, sex);
        }
 
        String uId = entity.getUId();
        if (uId != null) {
            stmt.bindString(6, uId);
        }
 
        String phone = entity.getPhone();
        if (phone != null) {
            stmt.bindString(7, phone);
        }
 
        String headImgUrl = entity.getHeadImgUrl();
        if (headImgUrl != null) {
            stmt.bindString(8, headImgUrl);
        }
 
        String intro = entity.getIntro();
        if (intro != null) {
            stmt.bindString(9, intro);
        }
 
        String grade = entity.getGrade();
        if (grade != null) {
            stmt.bindString(10, grade);
        }
 
        String clientId = entity.getClientId();
        if (clientId != null) {
            stmt.bindString(11, clientId);
        }
 
        String lastId = entity.getLastId();
        if (lastId != null) {
            stmt.bindString(12, lastId);
        }
 
        String integral = entity.getIntegral();
        if (integral != null) {
            stmt.bindString(13, integral);
        }
 
        String token = entity.getToken();
        if (token != null) {
            stmt.bindString(14, token);
        }
        stmt.bindLong(15, entity.getState() ? 1L: 0L);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public StudentInfo readEntity(Cursor cursor, int offset) {
        StudentInfo entity = new StudentInfo( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // studentNo
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // LengthOfSchooling
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // nickName
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // sex
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // uId
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // phone
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // headImgUrl
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // intro
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // grade
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // clientId
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // lastId
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // integral
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // token
            cursor.getShort(offset + 14) != 0 // state
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, StudentInfo entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setStudentNo(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setLengthOfSchooling(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setNickName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setSex(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setUId(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setPhone(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setHeadImgUrl(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setIntro(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setGrade(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setClientId(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setLastId(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setIntegral(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setToken(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setState(cursor.getShort(offset + 14) != 0);
     }
    
    @Override
    protected final Long updateKeyAfterInsert(StudentInfo entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(StudentInfo entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(StudentInfo entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
