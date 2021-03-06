'use strict';
module.exports = function(sequelize, DataTypes) {
  var orders = sequelize.define('orders', {
    userid: DataTypes.STRING,
    orderid: DataTypes.STRING,
    valid: DataTypes.INTEGER,
    contact: DataTypes.STRING,
    gender:DataTypes.INTEGER,
    tel: DataTypes.STRING,
    address:DataTypes.STRING,
    province:DataTypes.STRING,
    city:DataTypes.STRING,
    area:DataTypes.STRING,
    shoeid:DataTypes.INTEGER,
    shoeName:DataTypes.STRING,
    size:DataTypes.INTEGER,
    color:DataTypes.STRING,
    type:DataTypes.STRING,
    price:DataTypes.INTEGER,
    remark:DataTypes.STRING,
    createtime:DataTypes.BIGINT,
    status:DataTypes.INTEGER
  },{
       timestamps: false
  }, {
    classMethods: {
      associate: function(models) {
        // associations can be defined here
      }
    }
  });
  return orders;
};