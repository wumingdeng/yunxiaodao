'use strict';
module.exports = function(sequelize, DataTypes) {
  var weight_diet_configs = sequelize.define('weight_diet_configs', {
    week: DataTypes.INTEGER,
    content: DataTypes.STRING
  }, {
       timestamps: false
  },{
    classMethods: {
      associate: function(models) {
        // associations can be defined here
      }
    }
  });
  return weight_diet_configs;
};