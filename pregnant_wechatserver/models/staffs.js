'use strict';
module.exports = function(sequelize, DataTypes) {
  var staffs = sequelize.define('staffs', {
    username: DataTypes.STRING,
    password: DataTypes.STRING,
    wxid: DataTypes.STRING,
    type: DataTypes.INTEGER
  },{
       timestamps: false
  }, {
    classMethods: {
      associate: function(models) {
        // associations can be defined here
      }
    }
  });
  return staffs;
};