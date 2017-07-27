'use strict';
module.exports = function(sequelize, DataTypes) {
  var foot_records = sequelize.define('foot_records', {
    userid: DataTypes.STRING,
    recordDate: DataTypes.INTEGER,
    picture: DataTypes.STRING,
    lfootLength:DataTypes.FLOAT,
    lfootWidth:DataTypes.FLOAT,
    lsize: DataTypes.INTEGER,
    ltypeWidth:DataTypes.INTEGER,
    lfootType:DataTypes.INTEGER,
    rfootLength:DataTypes.FLOAT,
    rfootWidth:DataTypes.FLOAT,
    rsize: DataTypes.INTEGER,
    rtypeWidth:DataTypes.INTEGER,
    rfootType:DataTypes.INTEGER,
    footJudgment:DataTypes.STRING,
    suggestShoe:DataTypes.INTEGER,
    footKnowledge:DataTypes.INTEGER
  },{
       timestamps: false
  }, {
    classMethods: {
      associate: function(models) {
        // associations can be defined here
      }
    }
  });
  return foot_records;
};