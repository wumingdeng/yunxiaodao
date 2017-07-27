'use strict';
module.exports = function(sequelize, DataTypes) {
  var footknowledge_configs = sequelize.define('footknowledge_configs', {
    minWeek: DataTypes.INTEGER,
    maxWeek: DataTypes.INTEGER,
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
  return footknowledge_configs;
};