'use strict';
module.exports = function(sequelize, DataTypes) {
  var settle_records = sequelize.define('settle_records', {
    wid: DataTypes.INTEGER,
    settled: DataTypes.FLOAT,
    oid: DataTypes.INTEGER,
    lastUpdated:DataTypes.BIGINT
  }, {
       timestamps: false
  },{
    classMethods: {
      associate: function(models) {
        // associations can be defined here
      }
    }
  });
  return settle_records;
};