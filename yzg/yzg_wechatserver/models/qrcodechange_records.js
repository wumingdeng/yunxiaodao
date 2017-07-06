'use strict';
module.exports = function(sequelize, DataTypes) {
  var qrcodechange_records = sequelize.define('qrcodechange_records', {
    userid: DataTypes.STRING,
    time: DataTypes.BIGINT,
    newSid: DataTypes.INTEGER,
    oldSid: DataTypes.INTEGER,
    newMaster: DataTypes.STRING,
    oldMaster: DataTypes.STRING
  },{
       timestamps: false
  }, {
    classMethods: {
      associate: function(models) {
        // associations can be defined here
      }
    }
  });
  return qrcodechange_records;
};