'use strict';
module.exports = function(sequelize, DataTypes) {
  var footType_advice_configs = sequelize.define('footType_advice_configs', {
    minWeek: DataTypes.INTEGER,
    maxWeek: DataTypes.INTEGER,
    footType: DataTypes.STRING,
    content: DataTypes.STRING
  },{
       timestamps: false
  }, {
    classMethods: {
      associate: function(models) {
        // associations can be defined here
      }
    }
  });
  return footType_advice_configs;
};