'use strict';
module.exports = function(sequelize, DataTypes) {
  var weightRate_configs = sequelize.define('weightRate_configs', {
    status: DataTypes.INTEGER,
    rateMin: DataTypes.FLOAT,
    rateMax:DataTypes.FLOAT,
    dRateMin:DataTypes.FLOAT,
    dRateMax:DataTypes.FLOAT
  }, {
       timestamps: false
  },{
    classMethods: {
      associate: function(models) {
        // associations can be defined here
      }
    }
  });
  return weightRate_configs;
};