package org.xxpay.dal.dao.model;

import java.io.Serializable;
import java.util.Date;

public class MchInfo implements Serializable {
    /**
     * 商户ID
     *
     * @mbggenerated
     */
    private String mchId;

    /**
     * 名称
     *
     * @mbggenerated
     */
    private String name;

    /**
     * 类型
     *
     * @mbggenerated
     */
    private String type;

    /**
     * 请求私钥
     *
     * @mbggenerated
     */
    private String reqKey;

    /**
     * 响应私钥
     *
     * @mbggenerated
     */
    private String resKey;

    /**
     * 商户状态,0-停止使用,1-使用中
     *
     * @mbggenerated
     */
    private Integer state;

    /**
     * 创建时间
     *
     * @mbggenerated
     */
    private Date createTime;

    /**
     * 更新时间
     *
     * @mbggenerated
     */
    private Date updateTime;

    /**
     * 负责人姓名
     */
    private String leaderName;

    /**
     * 负责人电话
     */
    private String leaderPhone;

    /**
     * 办公地址
     */
    private String officeAdress;

    /**
     * 额外参数
     */
    private String extra;
    private static final long serialVersionUID = 1L;


    public MchInfo(String mchId, String name, String type, String reqKey, String resKey, Integer state, Date createTime, Date updateTime, String leaderName, String leaderPhone, String officeAdress, String extra) {
        this.mchId = mchId;
        this.name = name;
        this.type = type;
        this.reqKey = reqKey;
        this.resKey = resKey;
        this.state = state;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.leaderName = leaderName;
        this.leaderPhone = leaderPhone;
        this.officeAdress = officeAdress;
        this.extra = extra;
    }

    public MchInfo() {
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReqKey() {
        return reqKey;
    }

    public void setReqKey(String reqKey) {
        this.reqKey = reqKey;
    }

    public String getResKey() {
        return resKey;
    }

    public void setResKey(String resKey) {
        this.resKey = resKey;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public String getLeaderPhone() {
        return leaderPhone;
    }

    public void setLeaderPhone(String leaderPhone) {
        this.leaderPhone = leaderPhone;
    }

    public String getOfficeAdress() {
        return officeAdress;
    }

    public void setOfficeAdress(String officeAdress) {
        this.officeAdress = officeAdress;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra == null ? null : extra.trim();
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", mchId=").append(mchId);
        sb.append(", name=").append(name);
        sb.append(", type=").append(type);
        sb.append(", reqKey=").append(reqKey);
        sb.append(", resKey=").append(resKey);
        sb.append(", state=").append(state);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        MchInfo other = (MchInfo) that;
        return (this.getMchId() == null ? other.getMchId() == null : this.getMchId().equals(other.getMchId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getReqKey() == null ? other.getReqKey() == null : this.getReqKey().equals(other.getReqKey()))
            && (this.getResKey() == null ? other.getResKey() == null : this.getResKey().equals(other.getResKey()))
            && (this.getState() == null ? other.getState() == null : this.getState().equals(other.getState()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getLeaderName() == null ? other.getLeaderName() == null : this.getLeaderName().equals(other.getLeaderName()))
            && (this.getLeaderPhone() == null ? other.getLeaderPhone() == null : this.getLeaderPhone().equals(other.getLeaderPhone()))
            && (this.getOfficeAdress() == null ? other.getOfficeAdress() == null : this.getOfficeAdress().equals(other.getOfficeAdress()))
            && (this.getExtra() == null ? other.getExtra() == null : this.getExtra().equals(other.getExtra()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getMchId() == null) ? 0 : getMchId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getReqKey() == null) ? 0 : getReqKey().hashCode());
        result = prime * result + ((getResKey() == null) ? 0 : getResKey().hashCode());
        result = prime * result + ((getState() == null) ? 0 : getState().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getLeaderName() == null) ? 0 : getLeaderName().hashCode());
        result = prime * result + ((getLeaderPhone() == null) ? 0 : getLeaderPhone().hashCode());
        result = prime * result + ((getOfficeAdress() == null) ? 0 : getOfficeAdress().hashCode());
        result = prime * result + ((getExtra() == null) ? 0 : getExtra().hashCode());
        return result;
    }

}