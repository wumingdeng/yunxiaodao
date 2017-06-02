'use strict';
module.exports = function(sequelize, DataTypes) {
  var users = sequelize.define('users', {
    wxid: DataTypes.STRING,
    userName: DataTypes.STRING,//账号名称
    password: DataTypes.STRING,//账号密码
    lastPeriod:DataTypes.BIGINT,//末次月经
    name:DataTypes.STRING,
    weight:DataTypes.FLOAT,//体重
    shape:DataTypes.INTEGER,
    height:DataTypes.FLOAT,//身高
    isSingle:DataTypes.INTEGER,
    contact:DataTypes.STRING,
    gender:DataTypes.INTEGER,
    tel:DataTypes.STRING,//注册电话
    address:DataTypes.STRING,
    city:DataTypes.STRING,
    headUrl:DataTypes.STRING,
    // from here is yxd
    card_id:DataTypes.STRING,
    subscribe_app:DataTypes.STRING,
    subscribe_time:DataTypes.BIGINT,//
    subscribe_status:DataTypes.INTEGER,
    file_amount:DataTypes.INTEGER,
    nation:DataTypes.STRING,
    date_birth:DataTypes.BIGINT,
    district:DataTypes.STRING,
    province:DataTypes.STRING,
    county:DataTypes.STRING,
    blood:DataTypes.STRING,
    // date_yunfu:DataTypes.STRING,
    user_email:DataTypes.STRING,//注册邮箱
    user_date:DataTypes.BIGINT//注册时间
  },{
       timestamps: false
  }, {
    classMethods: {
      associate: function(models) {
        // associations can be defined here
      }
    }
  });
  return users;
};