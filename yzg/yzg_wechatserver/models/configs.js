'use strict';
module.exports = function(sequelize, DataTypes) {
  var configs = sequelize.define('configs', {
    payExpiredTime: DataTypes.INTEGER,
    orderWaitTime: DataTypes.INTEGER,
    userCancelFeeA: DataTypes.INTEGER,
    userCancelFeeB: DataTypes.INTEGER,
    longDistanceFee:DataTypes.INTEGER,
    distanceConstraint:DataTypes.INTEGER,
    longDistanceBound:DataTypes.INTEGER,
    minPay:DataTypes.INTEGER,
    orderSettlePercent:DataTypes.INTEGER
  },{
       timestamps: false
  }, {
    classMethods: {
      associate: function(models) {
        // associations can be defined here
      }
    }
  });
  return configs;
};