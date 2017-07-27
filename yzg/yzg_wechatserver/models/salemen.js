'use strict';
module.exports = function(sequelize, DataTypes) {
  var salemans = sequelize.define('salemans', {
    userid: DataTypes.STRING,
    realName: DataTypes.STRING,
    nickName: DataTypes.STRING,
    review: DataTypes.INTEGER,
    headUrl: DataTypes.STRING,
    phone: DataTypes.INTEGER,
    job: DataTypes.INTEGER,
    hospital: DataTypes.STRING,
    department:DataTypes.STRING,
    upid: DataTypes.STRING,
    joinDate: DataTypes.INTEGER
  },{
       timestamps: false
  }, {
    classMethods: {
      associate: function(models) {
        // associations can be defined here
      }
    }
  });
  return salemans;
};