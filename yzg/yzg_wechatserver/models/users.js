'use strict';
module.exports = function(sequelize, DataTypes) {
  var users = sequelize.define('users', {
    wxid: DataTypes.STRING,
    userName: DataTypes.STRING,
    password: DataTypes.STRING,
    lastPeriod:DataTypes.BIGINT,
    name:DataTypes.STRING,
    weight:DataTypes.FLOAT,
    shape:DataTypes.INTEGER,
    height:DataTypes.FLOAT,
    isSingle:DataTypes.INTEGER,
    contact:DataTypes.STRING,
    gender:DataTypes.INTEGER,
    tel:DataTypes.STRING,
    address:DataTypes.STRING,
    province:DataTypes.STRING,
    city:DataTypes.STRING,
    area:DataTypes.STRING,
    headUrl:DataTypes.STRING
  },{
       timestamps: false
  }, {
    classMethods: {
      associate: function(models) {
        // associations can be defined here
      }
    }
  });
  return users;
};