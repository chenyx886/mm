package com.mm.data.cache;


import android.util.Log;

import com.mm.MApplication;
import com.mm.data.entity.NodeEntity;
import com.mm.data.entity.UserEntity;
import com.mm.data.gen.CNode;
import com.mm.data.gen.CNodeDao;
import com.mm.data.gen.DaoMaster;
import com.mm.data.gen.DaoSession;
import com.mm.data.gen.UserCEntity;
import com.mm.data.gen.UserCoorEntity;
import com.mm.data.gen.UserCoorEntityDao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Chenyx
 * @ClassName: DBHelper
 * @Description: 数据库帮助类
 * @date 2017年4月1日 上午10:45:35
 */
public class DBHelper {

    private static final String TAG = "DBHelper";

    private static DBHelper instance;

    private DaoMaster daoMaster;

    private DaoSession daoSession;

    public synchronized static DBHelper getInstance(MApplication mApplication) {

        if (instance == null) {
            instance = new DBHelper(mApplication);
        }
        return instance;
    }

    /**
     * 构建DBHepler,初始化 daoMaster,daoSession
     *
     * @param mApplication
     */
    private DBHelper(MApplication mApplication) {

        this.daoMaster = mApplication.daoMaster;
        this.daoSession = mApplication.daoSession;
    }


    /**
     * 新增一条栏目数据缓存
     *
     * @param node
     */
    public void insertNodeEntity(NodeEntity node) {
        long result = daoSession.getCNodeDao().insertOrReplace(Node2CNode(node));
    }

    /**
     * 删除所有栏目缓存
     */
    public void deleteNodeAllEntity() {
        daoSession.getCNodeDao().deleteAll();
    }

    /**
     * 删除单条栏目缓存
     */
    public void deleteNodeEntity(int NodeID) {
        CNode cNode = queryCNode(NodeID);
        daoSession.getCNodeDao().deleteByKey(cNode.getId());
    }

    /**
     * 查询是否存在栏目
     *
     * @param NodeID
     * @return
     */
    public CNode queryCNode(int NodeID) {
        CNode cNode = daoSession.getCNodeDao().queryBuilder()
                .where(CNodeDao.Properties.NodeID.eq(NodeID))
                .build().unique();
        return cNode;
    }


    /**
     * 获取缓存栏目
     *
     * @return
     */
    public List<NodeEntity> queryNodeCEntyties() {

        Log.d(TAG, "into queryColumnCEntyties()");
        List<CNode> usEntities = daoSession.getCNodeDao().queryBuilder().orderAsc(CNodeDao.Properties.Time).list();
        List<NodeEntity> userEntities = new ArrayList<>();
        for (int i = 0; i < usEntities.size(); i++) {
            userEntities.add(cNode2Node(usEntities.get(i)));
        }
        Log.d(TAG, "userEntities size ：" + userEntities.size());
        Log.d(TAG, "into queryColumnCEntyties()");
        return userEntities;

    }


    private NodeEntity cNode2Node(CNode cNode) {
        NodeEntity node = new NodeEntity();
        node.setNodeID(cNode.getNodeID());
        node.setNodeName(cNode.getNodeName());
        return node;
    }


    private CNode Node2CNode(NodeEntity entity) {
        CNode cNode = new CNode();
        cNode.setNodeID(entity.getNodeID());
        cNode.setNodeName(entity.getNodeName());
        cNode.setTime(new Date());
        return cNode;
    }




    /**
     * 缓存单条用户信息
     *
     * @param emp
     */
    public void insertEmpCEntity(UserEntity emp) {

        UserCEntity temp = userEntity2EmpCEntity(emp);
        daoSession.getUserCEntityDao().insertOrReplace(temp);

    }


    /**
     * 缓存新增一条异常位置信息（1.位置信息定位失败 2.位置信息上传服务器失败）
     *
     * @param empCoorEntity
     */
    public void inertUserCoor(UserCoorEntity empCoorEntity) {
        daoSession.getUserCoorEntityDao().insert(empCoorEntity);

    }

    /**
     * 删除缓存的异常位置信息
     *
     * @param empCoorEntity
     */
    public void deleteUserCoors(UserCoorEntity empCoorEntity) {

        daoSession.getUserCoorEntityDao().delete(empCoorEntity);
    }

    /**
     * 分页查询异常位置信息
     *
     * @param limit
     * @return
     */
    public List<UserCoorEntity> queryUserCoorPage(int limit, String userId) {

        List<UserCoorEntity> empCoorEntities = daoSession.getUserCoorEntityDao().queryBuilder()
                .where(UserCoorEntityDao.Properties.UserId.eq(userId)).limit(limit)
                .orderAsc(UserCoorEntityDao.Properties.LctTime).build().list();
        return empCoorEntities;
    }


    /**
     * 是否有缓存位置信息
     *
     * @return
     */
    public boolean hasUserCoorCahche() {
        long count = daoSession.getUserCoorEntityDao().count();
        return count > 0;

    }

    private UserCEntity userEntity2EmpCEntity(UserEntity emp) {

        UserCEntity empCEntity = new UserCEntity();
        empCEntity.setPhone(emp.getPhone());
        return empCEntity;
    }

}
