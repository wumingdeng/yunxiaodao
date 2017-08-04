'use strict';
module.exports = function(sequelize, DataTypes) {
  var salemans = sequelize.define('salemans', {
    userid: DataTypes.STRING,
    realName: DataTypes.STRING,
    nickName: DataTypes.STRING,
    review: DataTypes.INTEGER,
    headUrl: DataTypes.STRING,
    phone: DataTypes.STRING,
    job: DataTypes.INTEGER,
    certificate: DataTypes.STRING,
    hospital: DataTypes.STRING,
    department:DataTypes.STRING,
    haveTGInfo: DataTypes.INTEGER,
    upid: DataTypes.STRING,
    joinDate: DataTypes.INTEGER,
    review: DataTypes.INTEGER
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