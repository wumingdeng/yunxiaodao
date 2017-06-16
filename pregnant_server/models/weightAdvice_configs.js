'use strict';
module.exports = function(sequelize, DataTypes) {
  var weightAdvice_configs = sequelize.define('weightAdvice_configs', {
    minweek: DataTypes.INTEGER,
    maxweek: DataTypes.INTEGER,
    weight_size: DataTypes.INTEGER, //体重评估结果
    type: DataTypes.INTEGER,
    con_diet: DataTypes.TEXT, //饮食注意
    con_sug: DataTypes.TEXT, //建议
    con_sign: DataTypes.TEXT, //建议
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