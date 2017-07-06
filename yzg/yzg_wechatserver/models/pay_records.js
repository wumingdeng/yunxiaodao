'use strict';
module.exports = function(sequelize, DataTypes) {
  var pay_records = sequelize.define('pay_records', {
    uid: DataTypes.INTEGER,
    money: DataTypes.INTEGER,
    date: DataTypes.INTEGER
  }, {
       timestamps: false
  },{
    classMethods: {
      associate: function(models) {
        // associations can be defined here
      }
    }
  });
  return pay_records;
};