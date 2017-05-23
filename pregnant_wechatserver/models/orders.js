'use strict';
module.exports = function(sequelize, DataTypes) {
  var orders = sequelize.define('orders', {
    userid: DataTypes.INTEGER,
    workerid: DataTypes.INTEGER,
    catalog: DataTypes.INTEGER,
    service: DataTypes.INTEGER,
    valid: DataTypes.INTEGER,
    payend: DataTypes.BIGINT,
    actualpay: DataTypes.INTEGER,
    needpay: DataTypes.INTEGER,
    address:DataTypes.STRING,
    extra:DataTypes.STRING,
    geo:DataTypes.GEOMETRY('POINT'),
    waitend:DataTypes.BIGINT,
    status:DataTypes.INTEGER,
    rate:DataTypes.FLOAT,
    price:DataTypes.INTEGER,
    startat:DataTypes.BIGINT,
    finishat:DataTypes.BIGINT,
    createtime:DataTypes.BIGINT
  }, {
       timestamps: false
  },{
    classMethods: {
      associate: function(models) {
        // associations can be defined here
      }
    }
  });
  return orders;
};