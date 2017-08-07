'use strict';
module.exports = function(sequelize, DataTypes) {
  var yzg_incomes = sequelize.define('yzg_incomes', {
    wxid: DataTypes.STRING,
    createtime: DataTypes.INTEGER,
    type: DataTypes.INTEGER,
    money: DataTypes.INTEGER
  },{
       timestamps: false
  }, {
    classMethods: {
      associate: function(models) {
        // associations can be defined here
      }
    }
  });
  return yzg_incomes;
};