'use strict';
module.exports = function(sequelize, DataTypes) {
  var weightAdvice_configs = sequelize.define('weightAdvice_configs', {
    minWeek: DataTypes.INTEGER,
    maxWeek: DataTypes.INTEGER,
    normal:DataTypes.STRING,
    skinny:DataTypes.STRING,
    fat:DataTypes.STRING,
    tip_normal:DataTypes.STRING,
    tip_skinny:DataTypes.STRING,
    tip_fat:DataTypes.STRING
  }, {
       timestamps: false
  },{
    classMethods: {
      associate: function(models) {
        // associations can be defined here
      }
    }
  });
  return weightAdvice_configs;
};