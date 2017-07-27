'use strict';
module.exports = function(sequelize, DataTypes) {
  var check_cycle_configs = sequelize.define('check_cycle_configs', {
    minWeek: DataTypes.INTEGER,
    maxWeek: DataTypes.INTEGER,
    attention: DataTypes.STRING,
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
  return check_cycle_configs;
};